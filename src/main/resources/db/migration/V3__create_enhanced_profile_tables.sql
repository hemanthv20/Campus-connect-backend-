-- ============================================
-- V3: Enhanced Student Profile System
-- Skills, Interests, Goals, Projects, Experience
-- ============================================

-- 1. Add new fields to users table
ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20);
ALTER TABLE users ADD COLUMN IF NOT EXISTS location VARCHAR(200);
ALTER TABLE users ADD COLUMN IF NOT EXISTS bio TEXT;

-- 2. SKILL CATEGORIES TABLE
CREATE TABLE IF NOT EXISTS skill_categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    icon VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default skill categories
INSERT INTO skill_categories (name, description, icon) VALUES
('Programming Languages', 'Programming and scripting languages', 'code'),
('Frameworks & Tools', 'Development frameworks and tools', 'tools'),
('Soft Skills', 'Communication and interpersonal skills', 'users'),
('Design', 'UI/UX and graphic design skills', 'palette'),
('Languages', 'Spoken and written languages', 'globe'),
('Data Science', 'Data analysis and machine learning', 'chart-bar'),
('Mobile Development', 'iOS and Android development', 'mobile'),
('DevOps', 'Deployment and infrastructure', 'server'),
('Other', 'Miscellaneous skills', 'star')
ON CONFLICT (name) DO NOTHING;

-- 3. SKILLS TABLE
CREATE TABLE IF NOT EXISTS skills (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    category_id INTEGER REFERENCES skill_categories(id) ON DELETE SET NULL,
    description TEXT,
    is_verified BOOLEAN DEFAULT FALSE,
    usage_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_skills_name ON skills(name);
CREATE INDEX IF NOT EXISTS idx_skills_category ON skills(category_id);

-- 4. USER SKILLS TABLE
CREATE TABLE IF NOT EXISTS user_skills (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    skill_id INTEGER NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    proficiency_level VARCHAR(20) NOT NULL CHECK (proficiency_level IN ('Beginner', 'Intermediate', 'Advanced', 'Expert')),
    years_experience INTEGER DEFAULT 0,
    certification_url TEXT,
    is_featured BOOLEAN DEFAULT FALSE,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, skill_id)
);

CREATE INDEX IF NOT EXISTS idx_user_skills_user_id ON user_skills(user_id);
CREATE INDEX IF NOT EXISTS idx_user_skills_skill_id ON user_skills(skill_id);

-- 5. SKILL ENDORSEMENTS TABLE
CREATE TABLE IF NOT EXISTS skill_endorsements (
    id SERIAL PRIMARY KEY,
    user_skill_id INTEGER NOT NULL REFERENCES user_skills(id) ON DELETE CASCADE,
    endorser_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_skill_id, endorser_id)
);

-- 6. INTEREST CATEGORIES TABLE
CREATE TABLE IF NOT EXISTS interest_categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    icon VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default interest categories
INSERT INTO interest_categories (name, description, icon) VALUES
('Technology & Innovation', 'Tech trends and innovations', 'cpu'),
('Arts & Culture', 'Creative arts and cultural activities', 'palette'),
('Sports & Fitness', 'Physical activities and sports', 'dumbbell'),
('Business & Entrepreneurship', 'Business and startup interests', 'briefcase'),
('Social Causes', 'Social impact and volunteering', 'heart'),
('Academic Research', 'Research and academic pursuits', 'book'),
('Hobbies & Entertainment', 'Personal hobbies and entertainment', 'gamepad'),
('Travel & Adventure', 'Travel and outdoor activities', 'map')
ON CONFLICT (name) DO NOTHING;

-- 7. INTERESTS TABLE
CREATE TABLE IF NOT EXISTS interests (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    category_id INTEGER REFERENCES interest_categories(id) ON DELETE SET NULL,
    description TEXT,
    usage_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_interests_name ON interests(name);
CREATE INDEX IF NOT EXISTS idx_interests_category ON interests(category_id);

-- 8. USER INTERESTS TABLE
CREATE TABLE IF NOT EXISTS user_interests (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    interest_id INTEGER NOT NULL REFERENCES interests(id) ON DELETE CASCADE,
    description TEXT,
    passion_level INTEGER DEFAULT 3 CHECK (passion_level >= 1 AND passion_level <= 5),
    is_featured BOOLEAN DEFAULT FALSE,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, interest_id)
);

CREATE INDEX IF NOT EXISTS idx_user_interests_user_id ON user_interests(user_id);
CREATE INDEX IF NOT EXISTS idx_user_interests_interest_id ON user_interests(interest_id);

-- 9. GOALS TABLE
CREATE TABLE IF NOT EXISTS goals (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL CHECK (category IN ('Career', 'Academic', 'Personal', 'Skill Development', 'Project', 'Other')),
    target_date DATE,
    status VARCHAR(20) DEFAULT 'Not Started' CHECK (status IN ('Not Started', 'In Progress', 'Completed', 'On Hold', 'Cancelled')),
    progress_percentage INTEGER DEFAULT 0 CHECK (progress_percentage >= 0 AND progress_percentage <= 100),
    is_public BOOLEAN DEFAULT TRUE,
    priority_level INTEGER DEFAULT 3 CHECK (priority_level >= 1 AND priority_level <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_goals_user_id ON goals(user_id);
CREATE INDEX IF NOT EXISTS idx_goals_status ON goals(status);

-- 10. GOAL MILESTONES TABLE
CREATE TABLE IF NOT EXISTS goal_milestones (
    id SERIAL PRIMARY KEY,
    goal_id INTEGER NOT NULL REFERENCES goals(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    target_date DATE,
    is_completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 11. PROJECTS TABLE
CREATE TABLE IF NOT EXISTS projects (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    technologies TEXT,
    github_url TEXT,
    demo_url TEXT,
    image_urls TEXT,
    start_date DATE,
    end_date DATE,
    status VARCHAR(20) DEFAULT 'In Progress' CHECK (status IN ('Planning', 'In Progress', 'Completed', 'On Hold', 'Cancelled')),
    is_featured BOOLEAN DEFAULT FALSE,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_projects_user_id ON projects(user_id);

-- 12. PROJECT COLLABORATORS TABLE
CREATE TABLE IF NOT EXISTS project_collaborators (
    id SERIAL PRIMARY KEY,
    project_id INTEGER NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    role VARCHAR(100),
    contribution_description TEXT,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(project_id, user_id)
);

-- 13. EDUCATION TABLE
CREATE TABLE IF NOT EXISTS education (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    institution_name VARCHAR(200) NOT NULL,
    degree VARCHAR(100),
    field_of_study VARCHAR(100),
    start_date DATE,
    end_date DATE,
    gpa DECIMAL(3,2),
    is_current BOOLEAN DEFAULT FALSE,
    achievements TEXT,
    relevant_coursework TEXT,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_education_user_id ON education(user_id);

-- 14. EXPERIENCE TABLE
CREATE TABLE IF NOT EXISTS experience (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    company_organization VARCHAR(200),
    experience_type VARCHAR(50) NOT NULL CHECK (experience_type IN ('Internship', 'Part-time Job', 'Full-time Job', 'Volunteer Work', 'Club Activity', 'Leadership Position', 'Other')),
    description TEXT,
    start_date DATE,
    end_date DATE,
    is_current BOOLEAN DEFAULT FALSE,
    location VARCHAR(100),
    achievements TEXT,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_experience_user_id ON experience(user_id);

-- 15. SOCIAL LINKS TABLE
CREATE TABLE IF NOT EXISTS social_links (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    platform VARCHAR(50) NOT NULL,
    url TEXT NOT NULL,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_social_links_user_id ON social_links(user_id);

-- 16. USER PROFILE EXTENSIONS TABLE
CREATE TABLE IF NOT EXISTS user_profile_extensions (
    id SERIAL PRIMARY KEY,
    user_id INTEGER UNIQUE NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    availability_status VARCHAR(20) DEFAULT 'Available' CHECK (availability_status IN ('Available', 'Busy', 'Not Looking')),
    looking_for TEXT,
    available_for TEXT,
    timezone VARCHAR(50),
    preferred_contact_method VARCHAR(50),
    response_time VARCHAR(50),
    profile_completion_percentage INTEGER DEFAULT 0,
    last_active TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 17. PROFILE VIEWS TABLE
CREATE TABLE IF NOT EXISTS profile_views (
    id SERIAL PRIMARY KEY,
    profile_user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    viewer_user_id INTEGER REFERENCES users(user_id) ON DELETE SET NULL,
    view_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address INET,
    user_agent TEXT
);

CREATE INDEX IF NOT EXISTS idx_profile_views_profile_user ON profile_views(profile_user_id);
CREATE INDEX IF NOT EXISTS idx_profile_views_date ON profile_views(view_date);

-- 18. COLLABORATION REQUESTS TABLE
CREATE TABLE IF NOT EXISTS collaboration_requests (
    id SERIAL PRIMARY KEY,
    requester_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    requested_user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    project_id INTEGER REFERENCES projects(id) ON DELETE CASCADE,
    goal_id INTEGER REFERENCES goals(id) ON DELETE CASCADE,
    message TEXT,
    request_type VARCHAR(50) NOT NULL CHECK (request_type IN ('Project Collaboration', 'Study Partner', 'Mentorship', 'Skill Exchange', 'General Networking')),
    status VARCHAR(20) DEFAULT 'Pending' CHECK (status IN ('Pending', 'Accepted', 'Declined', 'Cancelled')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_collaboration_requests_requester ON collaboration_requests(requester_id);
CREATE INDEX IF NOT EXISTS idx_collaboration_requests_requested ON collaboration_requests(requested_user_id);

-- Insert sample skills
INSERT INTO skills (name, category_id) VALUES
('Python', 1), ('JavaScript', 1), ('Java', 1), ('C++', 1), ('TypeScript', 1),
('React', 2), ('Django', 2), ('Spring Boot', 2), ('Node.js', 2), ('Angular', 2),
('Leadership', 3), ('Communication', 3), ('Teamwork', 3), ('Problem Solving', 3),
('UI/UX Design', 4), ('Photoshop', 4), ('Figma', 4), ('Video Editing', 4),
('English', 5), ('Spanish', 5), ('French', 5), ('German', 5),
('Machine Learning', 6), ('Data Analysis', 6), ('Deep Learning', 6), ('Statistics', 6),
('iOS Development', 7), ('Android Development', 7), ('React Native', 7), ('Flutter', 7),
('Docker', 8), ('Kubernetes', 8), ('AWS', 8), ('CI/CD', 8)
ON CONFLICT (name) DO NOTHING;

-- Insert sample interests
INSERT INTO interests (name, category_id) VALUES
('Artificial Intelligence', 1), ('Web Development', 1), ('Blockchain', 1), ('Cybersecurity', 1),
('Photography', 2), ('Music', 2), ('Painting', 2), ('Theater', 2),
('Football', 3), ('Basketball', 3), ('Yoga', 3), ('Running', 3),
('Entrepreneurship', 4), ('Startups', 4), ('Marketing', 4), ('Finance', 4),
('Environmental Conservation', 5), ('Community Service', 5), ('Education', 5),
('Research', 6), ('Publications', 6), ('Academic Writing', 6),
('Gaming', 7), ('Reading', 7), ('Cooking', 7), ('Gardening', 7),
('Travel', 8), ('Hiking', 8), ('Adventure Sports', 8), ('Camping', 8)
ON CONFLICT (name) DO NOTHING;

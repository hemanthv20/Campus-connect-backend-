-- V4: Seed Skills and Interests Data

-- Insert Skills (Programming Languages)
INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Java', id, true FROM skill_categories WHERE name = 'Programming Languages'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Python', id, true FROM skill_categories WHERE name = 'Programming Languages'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'JavaScript', id, true FROM skill_categories WHERE name = 'Programming Languages'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'TypeScript', id, true FROM skill_categories WHERE name = 'Programming Languages'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'C++', id, true FROM skill_categories WHERE name = 'Programming Languages'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'C#', id, true FROM skill_categories WHERE name = 'Programming Languages'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Go', id, true FROM skill_categories WHERE name = 'Programming Languages'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'PHP', id, true FROM skill_categories WHERE name = 'Programming Languages'
ON CONFLICT (name) DO NOTHING;

-- Insert Skills (Frameworks & Tools)
INSERT INTO skills (name, category_id, is_verified) 
SELECT 'React', id, true FROM skill_categories WHERE name = 'Frameworks & Tools'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Angular', id, true FROM skill_categories WHERE name = 'Frameworks & Tools'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Vue.js', id, true FROM skill_categories WHERE name = 'Frameworks & Tools'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Node.js', id, true FROM skill_categories WHERE name = 'Frameworks & Tools'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Spring Boot', id, true FROM skill_categories WHERE name = 'Frameworks & Tools'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Django', id, true FROM skill_categories WHERE name = 'Frameworks & Tools'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Express.js', id, true FROM skill_categories WHERE name = 'Frameworks & Tools'
ON CONFLICT (name) DO NOTHING;

-- Insert Skills (Soft Skills)
INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Communication', id, true FROM skill_categories WHERE name = 'Soft Skills'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Leadership', id, true FROM skill_categories WHERE name = 'Soft Skills'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Teamwork', id, true FROM skill_categories WHERE name = 'Soft Skills'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Problem Solving', id, true FROM skill_categories WHERE name = 'Soft Skills'
ON CONFLICT (name) DO NOTHING;

-- Insert Skills (Design)
INSERT INTO skills (name, category_id, is_verified) 
SELECT 'UI/UX Design', id, true FROM skill_categories WHERE name = 'Design'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Figma', id, true FROM skill_categories WHERE name = 'Design'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Photoshop', id, true FROM skill_categories WHERE name = 'Design'
ON CONFLICT (name) DO NOTHING;

-- Insert Skills (Data Science)
INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Machine Learning', id, true FROM skill_categories WHERE name = 'Data Science'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Data Analysis', id, true FROM skill_categories WHERE name = 'Data Science'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'TensorFlow', id, true FROM skill_categories WHERE name = 'Data Science'
ON CONFLICT (name) DO NOTHING;

-- Insert Skills (Mobile Development)
INSERT INTO skills (name, category_id, is_verified) 
SELECT 'React Native', id, true FROM skill_categories WHERE name = 'Mobile Development'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Flutter', id, true FROM skill_categories WHERE name = 'Mobile Development'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Android Development', id, true FROM skill_categories WHERE name = 'Mobile Development'
ON CONFLICT (name) DO NOTHING;

-- Insert Skills (DevOps)
INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Docker', id, true FROM skill_categories WHERE name = 'DevOps'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'Kubernetes', id, true FROM skill_categories WHERE name = 'DevOps'
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category_id, is_verified) 
SELECT 'AWS', id, true FROM skill_categories WHERE name = 'DevOps'
ON CONFLICT (name) DO NOTHING;

-- Insert Interests (Technology & Innovation)
INSERT INTO interests (name, category_id) 
SELECT 'Artificial Intelligence', id FROM interest_categories WHERE name = 'Technology & Innovation'
ON CONFLICT (name) DO NOTHING;

INSERT INTO interests (name, category_id) 
SELECT 'Web Development', id FROM interest_categories WHERE name = 'Technology & Innovation'
ON CONFLICT (name) DO NOTHING;

INSERT INTO interests (name, category_id) 
SELECT 'Mobile Apps', id FROM interest_categories WHERE name = 'Technology & Innovation'
ON CONFLICT (name) DO NOTHING;

-- Insert Interests (Arts & Culture)
INSERT INTO interests (name, category_id) 
SELECT 'Music', id FROM interest_categories WHERE name = 'Arts & Culture'
ON CONFLICT (name) DO NOTHING;

INSERT INTO interests (name, category_id) 
SELECT 'Photography', id FROM interest_categories WHERE name = 'Arts & Culture'
ON CONFLICT (name) DO NOTHING;

-- Insert Interests (Sports & Fitness)
INSERT INTO interests (name, category_id) 
SELECT 'Football', id FROM interest_categories WHERE name = 'Sports & Fitness'
ON CONFLICT (name) DO NOTHING;

INSERT INTO interests (name, category_id) 
SELECT 'Basketball', id FROM interest_categories WHERE name = 'Sports & Fitness'
ON CONFLICT (name) DO NOTHING;

INSERT INTO interests (name, category_id) 
SELECT 'Yoga', id FROM interest_categories WHERE name = 'Sports & Fitness'
ON CONFLICT (name) DO NOTHING;

-- Insert Interests (Business & Entrepreneurship)
INSERT INTO interests (name, category_id) 
SELECT 'Startups', id FROM interest_categories WHERE name = 'Business & Entrepreneurship'
ON CONFLICT (name) DO NOTHING;

INSERT INTO interests (name, category_id) 
SELECT 'Marketing', id FROM interest_categories WHERE name = 'Business & Entrepreneurship'
ON CONFLICT (name) DO NOTHING;

# Password Migration Guide

## ⚠️ Important: Password Security Update

The CampusConnect application has been updated to use **BCrypt password hashing** for enhanced security. This means:

- ✅ Passwords are now securely hashed before storage
- ✅ Plain text passwords are no longer stored in the database
- ✅ Authentication is more secure

---

## 🔄 For Existing Installations

If you have existing users in your database with **plain text passwords**, you need to migrate them. Choose one of the options below:

---

## Option 1: Fresh Start (Recommended for Development)

### Steps:

1. **Backup your database** (if needed)
2. **Truncate the users table**:
   ```sql
   TRUNCATE TABLE users CASCADE;
   ```
3. **Have all users re-register** with the new system
4. Passwords will automatically be hashed

### Pros:

- ✅ Clean slate
- ✅ No migration code needed
- ✅ Guaranteed security

### Cons:

- ❌ Users must re-register
- ❌ Loses existing user data

---

## Option 2: Automated Migration (Recommended for Production)

### Step 1: Add Migration Endpoint

Add this temporary endpoint to `UserController.java`:

```java
@PostMapping("/admin/migrate-passwords")
public ResponseEntity<String> migratePasswords() {
    // Only allow admin access
    // Add authentication check here

    List<Users> users = service.getUsers();
    int migratedCount = 0;

    for (Users user : users) {
        String currentPassword = user.getPassword();

        // Check if password is already hashed (BCrypt hashes start with $2a$)
        if (currentPassword != null && !currentPassword.startsWith("$2a$")) {
            // This is a plain text password, hash it
            user.setPassword(currentPassword); // Will be hashed by service
            service.updateUser(user);
            migratedCount++;
        }
    }

    return ResponseEntity.ok("Successfully migrated " + migratedCount + " passwords");
}
```

### Step 2: Run Migration

1. **Start your backend server**
2. **Call the migration endpoint** using Postman or curl:
   ```bash
   curl -X POST http://localhost:8081/admin/migrate-passwords
   ```
3. **Verify the response** shows the number of migrated passwords
4. **Test login** with existing credentials
5. **Remove the migration endpoint** after successful migration

### Step 3: Verify Migration

Test login with an existing user:

```bash
curl -X POST http://localhost:8081/login \
  -d "username=testuser&password=testpassword"
```

If successful, you'll receive the user object.

### Pros:

- ✅ Preserves existing users
- ✅ No user action required
- ✅ Can be done during maintenance window

### Cons:

- ❌ Requires temporary code
- ❌ Needs testing before production

---

## Option 3: Manual SQL Migration

### ⚠️ Advanced Users Only

If you want to hash passwords directly in the database:

### Step 1: Create a Temporary Java Program

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Replace with actual passwords from your database
        String[] plainPasswords = {
            "password123",
            "user456",
            "admin789"
        };

        for (String password : plainPasswords) {
            String hashed = encoder.encode(password);
            System.out.println("Plain: " + password);
            System.out.println("Hashed: " + hashed);
            System.out.println("---");
        }
    }
}
```

### Step 2: Update Database

```sql
-- Example: Update specific user
UPDATE users
SET password = '$2a$10$hashedPasswordHere'
WHERE username = 'testuser';

-- Repeat for each user
```

### Pros:

- ✅ Full control over migration
- ✅ Can be done offline

### Cons:

- ❌ Manual and error-prone
- ❌ Time-consuming for many users
- ❌ Requires database access

---

## 🧪 Testing After Migration

### Test 1: Existing User Login

```bash
# Should succeed with old password
curl -X POST http://localhost:8081/login \
  -d "username=existinguser&password=oldpassword"
```

### Test 2: New User Registration

```bash
# Should automatically hash password
curl -X POST http://localhost:8081/createuser \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "newpassword123",
    "first_name": "New",
    "last_name": "User",
    "email": "new@example.com",
    "gender": "Other",
    "college": "Test College",
    "semester": "1st",
    "batch": "2025"
  }'
```

### Test 3: Password Update

```bash
# Should hash new password
curl -X PUT http://localhost:8081/updateuser \
  -H "Content-Type: application/json" \
  -d '{
    "user_id": 1,
    "password": "newpassword456",
    ...other fields...
  }'
```

---

## 🔍 Verification Checklist

After migration, verify:

- [ ] Existing users can log in with their old passwords
- [ ] New users can register successfully
- [ ] Passwords in database start with `$2a$` (BCrypt format)
- [ ] Password updates work correctly
- [ ] Authentication fails with wrong passwords
- [ ] No plain text passwords remain in database

---

## 🔒 Security Best Practices

### After Migration:

1. **Remove Migration Endpoint**: Delete the temporary migration code
2. **Audit Database**: Verify no plain text passwords remain
3. **Update Documentation**: Note the migration date
4. **Monitor Logs**: Watch for authentication issues
5. **Backup Database**: Keep a backup before and after migration

### Going Forward:

1. **Never log passwords**: Remove any password logging
2. **Use HTTPS**: Always use HTTPS in production
3. **Rate Limiting**: Implement login attempt limiting
4. **Session Management**: Use secure session tokens
5. **Regular Updates**: Keep dependencies updated

---

## 📊 Migration Script Example

Here's a complete migration script you can use:

```java
package com.socialmediaweb.socialmediaweb.migration;

import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

// Uncomment @Component to run on startup
// @Component
public class PasswordMigration implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting password migration...");

        List<Users> users = userRepository.findAll();
        int migratedCount = 0;

        for (Users user : users) {
            String currentPassword = user.getPassword();

            if (currentPassword != null && !currentPassword.startsWith("$2a$")) {
                // Hash the plain text password
                user.setPassword(passwordEncoder.encode(currentPassword));
                userRepository.save(user);
                migratedCount++;
                System.out.println("Migrated password for user: " + user.getUsername());
            }
        }

        System.out.println("Password migration complete. Migrated " + migratedCount + " passwords.");
    }
}
```

To use:

1. Create this file in your project
2. Uncomment `@Component`
3. Start the application once
4. Comment out `@Component` again
5. Restart the application

---

## ❓ FAQ

### Q: Will existing users need to reset their passwords?

**A:** No, if you use Option 2 (Automated Migration), existing passwords will work as-is.

### Q: How do I know if migration was successful?

**A:** Check the database - all passwords should start with `$2a$` (BCrypt format).

### Q: Can I roll back if something goes wrong?

**A:** Yes, if you backed up your database before migration. Restore from backup.

### Q: What if I have thousands of users?

**A:** Use Option 2 with a batch processing approach. Migrate in chunks during low-traffic periods.

### Q: Is BCrypt the best option?

**A:** Yes, BCrypt is industry-standard for password hashing. It's secure, well-tested, and adaptive.

---

## 🆘 Troubleshooting

### Issue: "Invalid username or password" after migration

**Solution**:

- Verify the password was actually hashed (check database)
- Ensure BCrypt dependency is in pom.xml
- Check that AuthenticationService is using passwordEncoder.matches()

### Issue: New users can't log in

**Solution**:

- Verify passwords are being hashed on registration
- Check saveUser() method in AuthenticationService
- Ensure passwordEncoder is initialized

### Issue: Migration endpoint returns error

**Solution**:

- Check database connection
- Verify all users have non-null passwords
- Check server logs for detailed error messages

---

## 📞 Support

If you encounter issues during migration:

1. **Check Logs**: Review backend console output
2. **Database Inspection**: Verify password format in database
3. **Test Endpoint**: Use Postman to test authentication
4. **Rollback**: Restore from backup if needed

---

## ✅ Post-Migration Checklist

- [ ] All existing users can log in
- [ ] New registrations work correctly
- [ ] Passwords are hashed in database
- [ ] Migration code removed/disabled
- [ ] Database backed up
- [ ] Documentation updated
- [ ] Team notified of changes
- [ ] Monitoring in place

---

**Remember**: Security is paramount. Take your time with migration and test thoroughly before deploying to production.

**Last Updated**: November 2025

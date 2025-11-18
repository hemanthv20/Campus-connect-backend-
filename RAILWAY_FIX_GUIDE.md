# 🔧 Railway Deployment Fix Guide

## ✅ What Was Fixed

### 1. Database Connection Issues

**Problem:** Backend couldn't connect to Railway's PostgreSQL
**Solution:** Created `DatabaseConfig.java` that properly parses Railway's `DATABASE_URL`

### 2. CORS Configuration

**Problem:** Frontend couldn't access backend API
**Solution:** Created `CorsConfig.java` with dynamic CORS origins

### 3. Application Properties

**Problem:** Hardcoded database credentials
**Solution:** Updated to use environment variables with fallbacks

---

## 🚂 Railway Deployment Steps

### Step 1: Ensure PostgreSQL Service is Running

```bash
1. In Railway dashboard, verify PostgreSQL service is active
2. Note the service name (usually "Postgres" or "PostgreSQL")
```

### Step 2: Link Services

```bash
1. Click on your backend service
2. Go to "Settings" tab
3. Scroll to "Service Variables"
4. Ensure DATABASE_URL is visible (auto-provided by Railway)
```

### Step 3: Set Environment Variables

In Railway backend service, add these variables:

**Required:**

```bash
PORT=8080
```

**Optional (for CORS):**

```bash
CORS_ORIGINS=https://your-frontend.vercel.app,https://your-frontend.netlify.app
```

**DO NOT SET THESE** (Railway auto-provides):

- ❌ DATABASE_URL
- ❌ PGHOST
- ❌ PGPORT
- ❌ PGDATABASE
- ❌ PGUSER
- ❌ PGPASSWORD

### Step 4: Deploy

```bash
# Push to GitHub (Railway auto-deploys)
git add .
git commit -m "fix: Railway database connection and CORS"
git push origin main

# Or use Railway CLI
railway up
```

---

## 🔍 Verify Deployment

### Check Logs

```bash
1. Go to Railway dashboard
2. Click on backend service
3. Go to "Deployments" tab
4. Click latest deployment
5. View logs
```

### Look for These Success Messages:

```
✅ "Parsing DATABASE_URL for Railway deployment"
✅ "Database URL: jdbc:postgresql://..."
✅ "Flyway migration completed successfully"
✅ "Started SocialmediaWebApplication"
```

### Test Endpoints

```bash
# Health check (if you have actuator)
curl https://your-backend.up.railway.app/actuator/health

# Test API endpoint
curl https://your-backend.up.railway.app/feed

# Test with frontend
# Open browser console and check network tab
```

---

## 🐛 Troubleshooting

### Issue 1: "Connection refused to localhost:5432"

**Cause:** DATABASE_URL not being read
**Fix:**

```bash
1. Check Railway logs for "DATABASE_URL not found"
2. Verify PostgreSQL service is linked
3. Check service variables in Railway dashboard
4. Restart backend service
```

### Issue 2: "Unable to determine Dialect"

**Cause:** Database connection not established
**Fix:**

```bash
1. Verify DATABASE_URL format in logs
2. Check PostgreSQL service is running
3. Ensure DatabaseConfig.java is being used
4. Check for any typos in DATABASE_URL parsing
```

### Issue 3: CORS Errors

**Cause:** Frontend URL not in allowed origins
**Fix:**

```bash
1. Set CORS_ORIGINS environment variable
2. Include both http and https versions
3. Format: https://app.vercel.app,https://app.netlify.app
4. Restart backend service
```

### Issue 4: Flyway Migration Fails

**Cause:** Database schema issues
**Fix:**

```bash
1. Check migration files in src/main/resources/db/migration
2. Verify SQL syntax
3. Check Railway PostgreSQL logs
4. May need to drop and recreate database
```

### Issue 5: Out of Memory

**Cause:** Railway free tier has 512MB RAM limit
**Fix:**

```bash
# Already fixed in railway.json with -Xmx512m
# If still issues, reduce connection pool:
spring.datasource.hikari.maximum-pool-size=3
spring.datasource.hikari.minimum-idle=1
```

---

## 📊 Environment Variable Checklist

### Railway Backend Service Variables:

| Variable               | Value            | Source            | Required       |
| ---------------------- | ---------------- | ----------------- | -------------- |
| DATABASE_URL           | postgresql://... | Auto (PostgreSQL) | ✅ Yes         |
| PORT                   | 8080             | Manual            | ✅ Yes         |
| CORS_ORIGINS           | https://...      | Manual            | ⚠️ Recommended |
| SPRING_PROFILES_ACTIVE | prod             | Manual            | ❌ Optional    |

### Railway PostgreSQL Service Variables:

| Variable     | Value                     | Source | Auto-Linked |
| ------------ | ------------------------- | ------ | ----------- |
| PGHOST       | postgres.railway.internal | Auto   | ✅ Yes      |
| PGPORT       | 5432                      | Auto   | ✅ Yes      |
| PGDATABASE   | railway                   | Auto   | ✅ Yes      |
| PGUSER       | postgres                  | Auto   | ✅ Yes      |
| PGPASSWORD   | <generated>               | Auto   | ✅ Yes      |
| DATABASE_URL | postgresql://...          | Auto   | ✅ Yes      |

---

## 🧪 Local Testing

### Test with Railway DATABASE_URL locally:

```bash
# Get DATABASE_URL from Railway dashboard
export DATABASE_URL="postgresql://postgres:password@host:5432/railway"

# Build and run
cd back-end
mvn clean package
java -jar target/*.jar

# Check logs for:
# "Parsing DATABASE_URL for Railway deployment"
# "Database URL: jdbc:postgresql://..."
```

### Test with local PostgreSQL:

```bash
# Don't set DATABASE_URL
unset DATABASE_URL

# Run application
mvn spring-boot:run

# Should use local fallback:
# jdbc:postgresql://localhost:5432/socialmedia_db
```

---

## 📝 Code Changes Summary

### Files Created:

1. `DatabaseConfig.java` - Parses DATABASE_URL
2. `CorsConfig.java` - Dynamic CORS configuration
3. `RAILWAY_FIX_GUIDE.md` - This guide

### Files Modified:

1. `application.properties` - Removed hardcoded credentials
2. `railway.json` - Added memory optimization
3. `nixpacks.toml` - Matching configuration

---

## ✨ Benefits of This Fix

- ✅ **Automatic DATABASE_URL parsing** - No manual configuration
- ✅ **Local development support** - Falls back to localhost
- ✅ **Secure** - No hardcoded credentials
- ✅ **Flexible CORS** - Environment-based configuration
- ✅ **Railway-optimized** - Memory limits and connection pooling
- ✅ **Debug-friendly** - Logs show connection details

---

## 🎯 Success Criteria

Your deployment is successful when:

- ✅ Backend starts without errors
- ✅ Database connection established
- ✅ Flyway migrations complete
- ✅ API endpoints respond
- ✅ Frontend can access backend (no CORS errors)
- ✅ Can create/read data from database

---

## 📞 Still Having Issues?

### Check These:

1. Railway PostgreSQL service is running
2. Services are linked in Railway dashboard
3. DATABASE_URL appears in backend service variables
4. Build logs show successful Maven build
5. Runtime logs show database connection success

### Get Help:

- Railway Discord: https://discord.gg/railway
- Railway Docs: https://docs.railway.app/
- GitHub Issues: Create issue in your repo

---

**Last Updated:** November 2025
**Status:** ✅ Fixed and Tested

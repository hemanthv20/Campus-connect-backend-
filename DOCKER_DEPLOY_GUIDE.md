# Docker Deployment Guide - CORS Fix

## Overview

This guide covers rebuilding and deploying your backend Docker image with CORS fixes for Netlify frontend.

## ✅ CORS Configuration Updated

The following URLs are now allowed:

- `https://69206b3a68d3500008f42b1c--gilded-semolina-097069.netlify.app` (Preview)
- `https://gilded-semolina-097069.netlify.app` (Production)
- `http://localhost:3000` (Local development)
- `http://localhost:3001` (Local development alternate)

---

## Task 3: Docker Commands

### Step 1: Navigate to Backend Directory

```bash
cd CampusConnect/back-end
```

### Step 2: Build the Docker Image

```bash
docker build -t hemanthv20/campusconnect-backend:latest .
```

**Expected output:**

```
[+] Building 45.2s (12/12) FINISHED
=> [internal] load build definition
=> => transferring dockerfile
...
=> exporting to image
=> => naming to docker.io/hemanthv20/campusconnect-backend:latest
```

### Step 3: Test Locally (Optional but Recommended)

```bash
# Run with environment variables
docker run -p 8080:8080 \
  -e DATA_SOURCE_URL=jdbc:postgresql://your-db-host:5432/socialmedia_db \
  -e DATA_SOURCE_USER=your_db_user \
  -e DATA_SOURCE_PASSWORD=your_db_password \
  hemanthv20/campusconnect-backend:latest
```

**Test CORS locally:**

```bash
# From another terminal
curl -H "Origin: https://gilded-semolina-097069.netlify.app" \
     -H "Access-Control-Request-Method: GET" \
     -H "Access-Control-Request-Headers: Content-Type" \
     -X OPTIONS \
     http://localhost:8080/feed -v
```

Look for: `Access-Control-Allow-Origin: https://gilded-semolina-097069.netlify.app`

### Step 4: Login to Docker Hub

```bash
docker login
```

Enter your credentials:

- Username: `hemanthv20`
- Password: [your Docker Hub password]

### Step 5: Push to Docker Hub

```bash
docker push hemanthv20/campusconnect-backend:latest
```

**Expected output:**

```
The push refers to repository [docker.io/hemanthv20/campusconnect-backend]
latest: digest: sha256:abc123... size: 2841
```

### Step 6: Tag with Version (Optional)

```bash
# Tag with version for rollback capability
docker tag hemanthv20/campusconnect-backend:latest hemanthv20/campusconnect-backend:v1.1-cors-fix

# Push version tag
docker push hemanthv20/campusconnect-backend:v1.1-cors-fix
```

---

## Task 4: Redeploy on Render

### Option A: Manual Deploy (Recommended for Testing)

1. **Go to Render Dashboard**

   - Navigate to: https://dashboard.render.com

2. **Select Your Service**

   - Click on your backend service (e.g., "campusconnect-backend")

3. **Trigger Manual Deploy**

   - Click "Manual Deploy" button (top right)
   - Select "Deploy latest commit" or "Clear build cache & deploy"
   - Click "Deploy"

4. **Monitor Deployment**

   - Watch the logs for:
     ```
     Starting service with 'docker run'...
     Pulling image hemanthv20/campusconnect-backend:latest
     Successfully pulled image
     ```

5. **Verify Service Started**
   - Look for: `Started SocialmediaWebApplication in X.XXX seconds`
   - Check: Service status shows "Live"

### Option B: Auto-Deploy from Git (If Using Source Deployment)

If you're deploying from GitHub (not Docker image):

```bash
# Commit changes
git add src/main/java/com/socialmediaweb/socialmediaweb/config/CorsConfig.java
git commit -m "Fix CORS: Add Netlify URLs to allowed origins"
git push origin main
```

Render will automatically detect the push and redeploy.

### Option C: Update Docker Image in Render

If Render is configured to pull from Docker Hub:

1. **Update Image Tag** (if needed)

   - Go to: Service Settings → Image
   - Ensure it's set to: `hemanthv20/campusconnect-backend:latest`

2. **Trigger Redeploy**
   - Render should auto-detect new image
   - Or manually trigger deploy from dashboard

---

## Task 5: Test CORS Fix

### Method 1: Browser DevTools (Easiest)

1. **Open your Netlify site:**

   ```
   https://gilded-semolina-097069.netlify.app
   ```

2. **Open Browser DevTools** (F12)

3. **Go to Console tab**

4. **Try an API call:**

   ```javascript
   fetch("https://your-backend-url.onrender.com/feed")
     .then((res) => res.json())
     .then((data) => console.log("Success!", data))
     .catch((err) => console.error("CORS Error:", err));
   ```

5. **Check for Success:**
   - ✅ No CORS errors in console
   - ✅ Data returned successfully
   - ❌ If still failing, check Network tab for response headers

### Method 2: CURL Test

```bash
# Test OPTIONS preflight request
curl -H "Origin: https://gilded-semolina-097069.netlify.app" \
     -H "Access-Control-Request-Method: POST" \
     -H "Access-Control-Request-Headers: Content-Type" \
     -X OPTIONS \
     https://your-backend-url.onrender.com/api/follow -v

# Look for these headers in response:
# Access-Control-Allow-Origin: https://gilded-semolina-097069.netlify.app
# Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
# Access-Control-Allow-Credentials: true
```

### Method 3: Online CORS Tester

1. Go to: https://www.test-cors.org/

2. **Remote URL:** `https://your-backend-url.onrender.com/feed`

3. **Request Headers:**

   ```
   Origin: https://gilded-semolina-097069.netlify.app
   ```

4. **Click "Send Request"**

5. **Check Results:**
   - ✅ Status: 200 OK
   - ✅ CORS headers present
   - ✅ No errors

### Method 4: Check Network Tab

1. **Open your Netlify app**

2. **Open DevTools → Network tab**

3. **Perform an action** (login, load feed, etc.)

4. **Click on API request**

5. **Check Response Headers:**
   ```
   Access-Control-Allow-Origin: https://gilded-semolina-097069.netlify.app
   Access-Control-Allow-Credentials: true
   Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
   ```

---

## Troubleshooting

### Issue: CORS Still Failing

**Check 1: Verify Image Was Rebuilt**

```bash
# Check image creation date
docker images hemanthv20/campusconnect-backend:latest

# Should show recent timestamp
```

**Check 2: Verify Render Pulled New Image**

- Check Render logs for: `Pulling image hemanthv20/campusconnect-backend:latest`
- Look for new digest hash

**Check 3: Clear Browser Cache**

```bash
# Hard refresh in browser
Ctrl + Shift + R (Windows/Linux)
Cmd + Shift + R (Mac)
```

**Check 4: Verify Backend URL**

- Ensure frontend is calling correct backend URL
- Check `REACT_APP_API_URL` in Netlify environment variables

### Issue: Docker Build Fails

**Error: "Cannot connect to Docker daemon"**

```bash
# Start Docker Desktop
# Or on Linux:
sudo systemctl start docker
```

**Error: "No space left on device"**

```bash
# Clean up old images
docker system prune -a
```

### Issue: Push to Docker Hub Fails

**Error: "denied: requested access to the resource is denied"**

```bash
# Login again
docker logout
docker login

# Verify repository exists on Docker Hub
# Create repository if needed at: https://hub.docker.com
```

---

## Quick Command Reference

```bash
# Full deployment workflow
cd CampusConnect/back-end
docker build -t hemanthv20/campusconnect-backend:latest .
docker push hemanthv20/campusconnect-backend:latest

# Then trigger manual deploy on Render dashboard

# Verify deployment
curl https://your-backend-url.onrender.com/feed
```

---

## Environment Variables on Render

Ensure these are set in Render:

```
DATA_SOURCE_URL=jdbc:postgresql://your-db-host:5432/socialmedia_db
DATA_SOURCE_USER=your_db_user
DATA_SOURCE_PASSWORD=your_db_password
FRONTEND_URL=https://gilded-semolina-097069.netlify.app
CORS_ALLOWED_ORIGINS=https://gilded-semolina-097069.netlify.app,http://localhost:3000
```

---

## Success Checklist

- [ ] CORS configuration updated with Netlify URLs
- [ ] Docker image built successfully
- [ ] Docker image pushed to Docker Hub
- [ ] Render service redeployed
- [ ] Backend service shows "Live" status
- [ ] CORS test passes (no errors in browser console)
- [ ] Frontend can make API calls successfully
- [ ] All features working on Netlify site

---

## Additional Notes

### Adding New Frontend URLs

If you deploy to a new URL, update `CorsConfig.java`:

```java
.allowedOrigins(
    "https://your-new-url.netlify.app",
    "https://gilded-semolina-097069.netlify.app",
    "http://localhost:3000"
)
```

Then rebuild and redeploy.

### Wildcard CORS (Not Recommended for Production)

For development/testing only:

```java
.allowedOriginPatterns("*")  // Allows all origins
```

**Security Warning:** Never use wildcards in production!

---

**Need Help?** Check Render logs for specific error messages.

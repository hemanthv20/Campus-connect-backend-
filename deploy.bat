@echo off
REM CampusConnect Backend - Docker Deploy Script (Windows)
REM This script builds and pushes the Docker image to Docker Hub

echo.
echo ================================
echo CampusConnect Backend Deployment
echo ================================
echo.

REM Configuration
set DOCKER_USERNAME=hemanthv20
set IMAGE_NAME=campusconnect-backend
set TAG=latest
set FULL_IMAGE=%DOCKER_USERNAME%/%IMAGE_NAME%:%TAG%

REM Step 1: Build Docker image
echo Step 1: Building Docker image...
docker build -t %FULL_IMAGE% .

if %ERRORLEVEL% NEQ 0 (
    echo Docker build failed!
    exit /b 1
)

echo Docker image built successfully!
echo.

REM Step 2: Tag with version
set VERSION_TAG=%DOCKER_USERNAME%/%IMAGE_NAME%:v1.1-cors-fix
echo Step 2: Tagging image as %VERSION_TAG%...
docker tag %FULL_IMAGE% %VERSION_TAG%
echo.

REM Step 3: Push to Docker Hub
echo Step 3: Pushing to Docker Hub...
echo Pushing %FULL_IMAGE%...
docker push %FULL_IMAGE%

if %ERRORLEVEL% NEQ 0 (
    echo Push failed! Make sure you're logged in: docker login
    exit /b 1
)

echo Latest tag pushed successfully!
echo.

echo Pushing %VERSION_TAG%...
docker push %VERSION_TAG%

echo.
echo ================================
echo Deployment Complete!
echo ================================
echo.
echo Next Steps:
echo 1. Go to Render Dashboard: https://dashboard.render.com
echo 2. Select your backend service
echo 3. Click 'Manual Deploy' - 'Deploy latest commit'
echo 4. Monitor logs for successful deployment
echo 5. Test CORS from your Netlify site
echo.
echo Your Docker image: %FULL_IMAGE%
echo.
pause
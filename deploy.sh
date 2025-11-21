#!/bin/bash

# CampusConnect Backend - Docker Deploy Script
# This script builds and pushes the Docker image to Docker Hub

set -e  # Exit on error

echo "🚀 CampusConnect Backend Deployment"
echo "===================================="
echo ""

# Configuration
DOCKER_USERNAME="hemanthv20"
IMAGE_NAME="campusconnect-backend"
TAG="latest"
FULL_IMAGE="${DOCKER_USERNAME}/${IMAGE_NAME}:${TAG}"

# Step 1: Build Docker image
echo "📦 Step 1: Building Docker image..."
docker build -t ${FULL_IMAGE} .

if [ $? -eq 0 ]; then
    echo "✅ Docker image built successfully!"
else
    echo "❌ Docker build failed!"
    exit 1
fi

echo ""

# Step 2: Tag with version (optional)
VERSION_TAG="${DOCKER_USERNAME}/${IMAGE_NAME}:v1.1-cors-fix"
echo "🏷️  Step 2: Tagging image as ${VERSION_TAG}..."
docker tag ${FULL_IMAGE} ${VERSION_TAG}

echo ""

# Step 3: Push to Docker Hub
echo "📤 Step 3: Pushing to Docker Hub..."
echo "Pushing ${FULL_IMAGE}..."
docker push ${FULL_IMAGE}

if [ $? -eq 0 ]; then
    echo "✅ Latest tag pushed successfully!"
else
    echo "❌ Push failed! Make sure you're logged in: docker login"
    exit 1
fi

echo "Pushing ${VERSION_TAG}..."
docker push ${VERSION_TAG}

echo ""
echo "✅ Deployment Complete!"
echo ""
echo "📋 Next Steps:"
echo "1. Go to Render Dashboard: https://dashboard.render.com"
echo "2. Select your backend service"
echo "3. Click 'Manual Deploy' → 'Deploy latest commit'"
echo "4. Monitor logs for successful deployment"
echo "5. Test CORS from your Netlify site"
echo ""
echo "🔗 Your Docker image: ${FULL_IMAGE}"
echo ""
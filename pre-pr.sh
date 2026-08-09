#!/bin/bash
set -e

echo "🔍 Running all linting checks..."

# Frontend
echo "📦 Checking frontend..."
cd frontend
npm run lint
cd ..

# Backend
echo "☕ Checking backend..."
cd backend
./mvnw checkstyle:check
cd ..

echo "✅ All checks passed! Ready for PR."
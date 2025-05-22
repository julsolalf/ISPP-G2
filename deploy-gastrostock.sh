#!/bin/bash
cd frontend
npm run build
cp -r build/* ../src/main/resources/static/
cd ..
gcloud app deploy
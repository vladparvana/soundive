@echo off
setlocal

set IMAGE_NAME=vladparvana/eureka-service:latest
set CONTAINER_NAME=eureka-service
set PORT=8761

echo ===================================================
echo [1/3] Pull Docker image: %IMAGE_NAME%
echo ===================================================
docker pull %IMAGE_NAME%

echo ===================================================
echo [2/3] Stop and remove existing container (if any)
echo ===================================================
docker stop %CONTAINER_NAME% >nul 2>&1
docker rm %CONTAINER_NAME% >nul 2>&1

echo ===================================================
echo [3/3] Run Eureka container on port %PORT%
echo ===================================================
docker run -d --name %CONTAINER_NAME% -p %PORT%:%PORT% %IMAGE_NAME%

echo 🟢 Eureka Server is running at: http://localhost:%PORT%
endlocal
pause

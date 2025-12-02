# Multi-stage build for React/Vite application
FROM node:18-alpine AS build

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Build argument for API URL
ARG VITE_PROD_API_BASE_URL
ENV VITE_PROD_API_BASE_URL=${VITE_PROD_API_BASE_URL}

# Build the application
RUN npm run build

# Runtime stage with nginx
FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*

# Copy built app from build stage
COPY --from=build /app/dist /usr/share/nginx/html

# Copy custom nginx config for React Router
COPY nginx/nginx.conf /etc/nginx/nginx.conf

# Expose port 80 (nginx default)
EXPOSE 3000

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]

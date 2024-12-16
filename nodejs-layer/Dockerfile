ARG BASE_IMAGE=adafef2e596e/base-image:latest
FROM ${BASE_IMAGE}

# Accept Node.js version as build argument with default
ARG nodejs_VERSION

# Set environment variables
ENV NODE_ENV=production \
    NPM_CONFIG_LOGLEVEL=error \
    DEBIAN_FRONTEND=noninteractive

# Switch to root for installations
USER root

# Create required directories with proper permissions
RUN mkdir -p /var/lib/apt/lists/partial

# Install Node.js and npm
RUN set -ex \
    && apt-get update \
    && mkdir -p /etc/apt/keyrings \
    && curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg \
    && echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_${nodejs_VERSION}.x nodistro main" | tee /etc/apt/sources.list.d/nodesource.list \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        nodejs \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    # Verify installation
    && node --version \
    && npm --version

# Create app directory and set permissions
RUN mkdir -p /app \
    && chown -R devops:devops /app

# Set working directory
WORKDIR /app

# Switch back to non-root user
USER devops

# Verify installation as non-root user
RUN node --version && npm --version

# Default command
CMD ["node", "-v"]
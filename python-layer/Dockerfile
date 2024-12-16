ARG BASE_IMAGE=adafef2e596e/base-image:latest
FROM ${BASE_IMAGE}

# Accept Python version as build argument
ARG PYTHON_VERSION

# Set environment variables
ENV PYTHONUNBUFFERED=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    DEBIAN_FRONTEND=noninteractive \
    POETRY_HOME="/opt/poetry" \
    POETRY_VIRTUALENVS_CREATE=false

# Switch to root for installations
USER root

# Create required directories with proper permissions
RUN mkdir -p /var/lib/apt/lists/partial

# Install Python and essential tools
RUN set -ex \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        python${PYTHON_VERSION} \
        python${PYTHON_VERSION}-dev \
        python${PYTHON_VERSION}-venv \
        python3-pip \
        python3-wheel \
        python3-setuptools \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    # Create Python symlinks
    && ln -sf /usr/bin/python${PYTHON_VERSION} /usr/local/bin/python \
    && ln -sf /usr/bin/python${PYTHON_VERSION} /usr/local/bin/python3 \
    # Upgrade pip
    && python -m pip install --no-cache-dir --upgrade pip \
    # Verify installation
    && python --version \
    && pip --version

# Create app directory and set permissions
RUN mkdir -p /app \
    && chown -R devops:devops /app

# Set working directory
WORKDIR /app

# Switch back to non-root user
USER devops

# Verify installation as non-root user
RUN python --version && pip --version

# Default command
CMD ["python", "-V"]
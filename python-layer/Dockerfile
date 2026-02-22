ARG BASE_IMAGE=adafef2e596e/base-image:latest
FROM ${BASE_IMAGE}

# Accept Python version as build argument
ARG PYTHON_VERSION

# Set environment variables
ENV PYTHONUNBUFFERED=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    DEBIAN_FRONTEND=noninteractive

# Switch to root for installations
USER root

# Install Python from deadsnakes PPA
RUN set -ex \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        software-properties-common \
    && add-apt-repository -y ppa:deadsnakes/ppa \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        python${PYTHON_VERSION} \
        python${PYTHON_VERSION}-dev \
        python${PYTHON_VERSION}-venv \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    && ln -sf /usr/bin/python${PYTHON_VERSION} /usr/local/bin/python \
    && ln -sf /usr/bin/python${PYTHON_VERSION} /usr/local/bin/python3 \
    # Install pip via get-pip.py for the target Python version
    # --break-system-packages needed on Ubuntu 24.04 (PEP 668)
    && curl -sS https://bootstrap.pypa.io/get-pip.py | python${PYTHON_VERSION} -- --break-system-packages \
    && pip install --no-cache-dir --break-system-packages setuptools wheel \
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

# Use base image
ARG BASE_IMAGE
FROM ${BASE_IMAGE}

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive

# Accept Python version as an argument
ARG PYTHON_VERSION

# Install Python and dependencies dynamically
RUN apt-get update && \
    apt-get install -y software-properties-common && \
    add-apt-repository ppa:deadsnakes/ppa && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        python${PYTHON_VERSION} python${PYTHON_VERSION}-dev python${PYTHON_VERSION}-distutils \
        wget && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install pip using get-pip.py
RUN wget https://bootstrap.pypa.io/get-pip.py && \
    python${PYTHON_VERSION} get-pip.py && \
    rm get-pip.py

# Set default Python version dynamically
RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python${PYTHON_VERSION} 1 && \
    update-alternatives --set python3 /usr/bin/python${PYTHON_VERSION}

# Verify installation
RUN python3 --version && pip3 --version

# Default command
CMD ["python3"]

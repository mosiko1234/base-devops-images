# Use your base image
ARG BASE_IMAGE
FROM ${BASE_IMAGE}

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive
ARG PYTHON_VERSION

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    software-properties-common && \
    add-apt-repository -y ppa:deadsnakes/ppa && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
    python3-six \
    python${PYTHON_VERSION} \
    python${PYTHON_VERSION}-venv \
    python${PYTHON_VERSION}-distutils \
    python3-pip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*



# Set default python and pip versions
RUN update-alternatives --remove python3 /usr/bin/python3 || true && \
    update-alternatives --install /usr/bin/python3 python3 /usr/bin/python${PYTHON_VERSION} 1 && \
    update-alternatives --set python3 /usr/bin/python${PYTHON_VERSION} && \
    ln -sf /usr/bin/python${PYTHON_VERSION}-pip /usr/local/bin/python${PYTHON_VERSION}-pip && \
    ln -sf /usr/bin/pip3 /usr/local/bin/pip3 || exit 1

# Verify installation
RUN python3 --version && pip3 --version || exit 1

CMD ["python3"]


ARG BASE_IMAGE
FROM ${BASE_IMAGE}

USER root

# Add Python installation
ARG PYTHON_VERSION


RUN echo "Using Python version: ${PYTHON_VERSION}" \
    apt-get update && apt-get install -y --no-install-recommends \
    software-properties-common \
    && add-apt-repository 'deb http://deb.debian.org/debian bullseye-backports main' \
    && apt-get update && apt-get install -y --no-install-recommends \
    python${PYTHON_VERSION} \
    python3-pip \
    python3-venv \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

## Upgrade pip and Install Common Tools
#RUN pip3 install --upgrade pip setuptools wheel --break-system-packages \
#    && pip3 install --break-system-packages \
#        virtualenv \
#        pytest \
#        pylint \
#        black \
#        flake8

CMD ["python3"]
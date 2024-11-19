# Use your base image
ARG BASE_IMAGE
FROM ${BASE_IMAGE}

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive
ARG PYTHON_VERSION

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    python3.11 python3.11-venv python3.11-distutils && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# הגדרת ברירת מחדל של python3
RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.11 1 && \
    update-alternatives --set python3 /usr/bin/python3.11


# Verify installation
RUN python3 --version && pip3 --version || exit 1

CMD ["python3"]


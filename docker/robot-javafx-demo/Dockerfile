FROM dorowu/ubuntu-desktop-lxde-vnc

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get -qq update && apt-get dist-upgrade -y  && apt-get install -qq --no-install-recommends --allow-unauthenticated -y \
  openssh-client \
  xterm \
  python-pip \
  git \
  python-setuptools \
  wget \
  openjdk-8-jre \
  openjfx \
  && apt-get clean && rm -rf /var/lib/apt/lists/*

COPY test.sh /bin/test.sh
RUN pip install --no-cache-dir \
  robotframework && chmod 555 /bin/test.sh


EXPOSE 5900 80
ENTRYPOINT ["/startup.sh"]

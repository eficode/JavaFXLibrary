FROM dorowu/ubuntu-desktop-lxde-vnc

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get -qq update && apt-get dist-upgrade -y  && apt-get install -qq --no-install-recommends --allow-unauthenticated -y \
  dbus \
  openssh-client \
  x11vnc \
  xvfb \
  bash \
  xterm \
  fluxbox \
  python-pip \
  git \
  nano \
  python-setuptools \
  wget \
  xvfb \
  xorg \
  openjdk-8-jre \
  openjfx \
  && apt-get clean && rm -rf /var/lib/apt/lists/*

COPY test.sh /bin/test.sh
RUN pip install --no-cache-dir \
  robotframework && chmod 555 /bin/test.sh

# JUST VNC
#COPY entrypoint.sh /entrypoint.sh
#RUN mkdir ~/.vnc && x11vnc -storepasswd 1234 ~/.vnc/passwd && chmod 555 /entrypoint.sh /bin/test.sh
#ENTRYPOINT ["x11vnc", "-create", "-forever", "-env", "FD_PROG=/entrypoint.sh", "-env", "X11VNC_CREATE_GEOM=${1:-1024x768x16}", "-usepw"]

EXPOSE 5900 80
ENTRYPOINT ["/startup.sh"]

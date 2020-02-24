FROM nginx
MAINTAINER ZHIWEI XU <jsrdxzw@gmail.com>

COPY logtime.sh /etc/nginx/logtime.sh
RUN apt-get update -y && apt-get install --yes --no-install-recommends apt-utils  && apt-get install cron -y
COPY clean-cron /etc/nginx/clean-cron
RUN chmod 755 /etc/nginx/logtime.sh && crontab /etc/nginx/clean-cron
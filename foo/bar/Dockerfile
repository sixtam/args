FROM docker.io/nginxinc/nginx-unprivileged:stable-alpine
RUN rm /etc/nginx/conf.d/default.conf
COPY ./nginx/ /etc/nginx/conf.d/
EXPOSE 8081

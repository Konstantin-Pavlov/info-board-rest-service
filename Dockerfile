# Use the official PostgreSQL image from the Docker Hub
FROM postgres:14.0

# Set environment variables
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=info_board

# Copy the SQL dump to the Docker image
COPY init.sql /docker-entrypoint-initdb.d/

# Expose the PostgreSQL port
EXPOSE 5432
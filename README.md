# 🎥 MovieExpert AI : Spring AI & RAG Learning Project

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-green?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring_AI-1.2.0-blue?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-ai)
[![Ollama](https://img.shields.io/badge/Ollama-Local_LLM-black?style=for-the-badge&logo=ollama)](https://ollama.ai/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-PGVector-336791?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)

A practice project designed to explore and demonstrate the capabilities of **Spring AI 1.2.0**. This application
implements a "Film Critic" assistant that combines a local LLM (Llama 3/Phi-3), a private knowledge base (RAG), and
external tools (Function Calling) to provide accurate movie insights.

The goal of this project is to showcase how to build a **Hybrid Cognitive Architecture** where the AI autonomously
decides whether to use internal knowledge, search a vector database, or call an external API.

---

## 🔥 Key Concepts Demonstrated

* **RAG (Retrieval-Augmented Generation):**
    * Implementation of a private knowledge base using **PGVector**.
    * Demonstrates how to inject custom data (e.g., a catalog of unreleased 2025 indie films) into the LLM's context.
* **Function Calling (Tools):**
    * Integration with the OMDb API to fetch real-time data like ratings and box office numbers.
    * Shows how the LLM can "act" by executing Java methods dynamically.
* **Conversation Memory:**
    * Implementation of `ChatMemory` to support follow-up questions and maintain context across a session.
* **Prompt Engineering:**
    * Examples of "Chain-of-Thought" prompting to guide the model through complex tasks (e.g., comparing a private RAG
      movie vs. a public classic movie).

---

## 🛠 Tech Stack

| Component        | Technology            | Description                                            |
|:-----------------|:----------------------|:-------------------------------------------------------|
| **Language**     | Java 21 (LTS)         | Modern syntax, Records, and Virtual Threads support.   |
| **Framework**    | Spring Boot 3.4.1     | Spring AI 1.2.0 for standardizing LLM interactions.    |
| **LLM Host**     | Ollama                | Runs Llama 3 or Phi-3 locally for zero-cost inference. |
| **Vector DB**    | PostgreSQL (pgvector) | Stores embeddings for the RAG knowledge base.          |
| **Embedding**    | Nomic-Embed-Text      | Model used to convert text into vector embeddings.     |
| **External API** | OMDb API              | Used for Function Calling demonstrations.              |

---

## 🚀 How to Run It

Follow these steps to run the project locally for testing and experimentation.

### Prerequisites

* Docker Desktop installed.
* Java 21 JDK.
* [Ollama](https://ollama.ai/) installed and running.
* An API Key from [OMDb](https://www.omdbapi.com/apikey.aspx) (Free tier).

### Step 1: Prepare the AI Models

Pull the required models to your local Ollama instance.

```bash
ollama pull llama3
ollama pull nomic-embed-text
```

### Step 2: Start Infrastructure

This project uses the spring-boot-docker-compose dependency. You do not need to manually run docker-compose up.

Simply ensure Docker Desktop is running in the background. When you start the Spring Boot application (Step 4), it will
automatically:

- Detect the compose.yaml file.

- Spin up the PostgreSQL container with the pgvector image.

- Configure the database connection details automatically.

### Step 3: Configuration

Modify application.properties file or set environment variables with your credentials.

```
spring.datasource.url=jdbc:postgresql://localhost:5432/moviedb
spring.datasource.username=myuser
spring.datasource.password=secret

# Your OMDb API Key
omdb.api.key=YOUR_KEY_HERE

# Model Configuration
spring.ai.ollama.chat.model=llama3
spring.ai.ollama.embedding.model=nomic-embed-text

```

### Step 4: Run the Application

Start the Spring Boot application. On startup, it will:

- Connect to the database.
- Ingest the sample indie-films-2025.json data into the Vector Store (if the table is empty).
- Expose the REST endpoints.

```bash
./mvnw spring-boot:run
```

## 📡 Functionalities & Usage

### 1. Structured Data (JSON)

Endpoint: `GET /movie-expert/suggestions/{genre}`

Example:

> GET /movie-expert/suggestions/horror

### 2. Tool Execution (Fact-Checking)

- Endpoint: `GET /movie-expert/chat`

Query Example:

> "What is the exact box office difference between Avatar and Avengers: Endgame?"

### 3. Hybrid Chat (RAG + Tools)

- Endpoint: `GET /movie-expert/indies`

Query Example:

> "I want two recommendations: one exclusive cyberpunk movie from your 2025 catalog, and one classic cyberpunk movie
> from the 80s."

What it demonstrates:

- Vector Search: Finds "Neon Garden" (2025) in the private DB.

- Logic: Brainstorms an 80s classic (e.g., "Blade Runner").

- Tool Execution: Calls OMDb to get Blade Runner's rating.

Synthesis: Combines all sources into one coherent answer.


---

Author
Agustin Marani

Software Engineer | Java, Software Architecture & AI Enthusiast

<a href="https://www.linkedin.com/in/agustinmaranidev/"> <img src="https://img.shields.io/badge/linkedin-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn"> </a> 


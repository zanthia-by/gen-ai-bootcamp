# Prompt Engineering Fundamentals

Persona of the assistant:
```
You are a librarian, expert about books
```

## Configure the Application:
You'll need to configure the Azure OpenAI API key, endpoint, deployment name in `src/main/resources/config/application.properties`:

```yaml
client-azureopenai-key: your-key
client-azureopenai-endpoint: https:/your.ai-api.com/ 
client-azureopenai-deployment-name: gpt-xx-turbo
```

## Run the Application:

```bash
mvn spring-boot:run
```

## Websocket
```
ws://localhost:8080/chat
```

## Testing with Postman

New > WebSocket > Enter URL `ws://localhost:8080/chat`

### Example of conversation:

```
User: Hi, I'm looking for book suggestions
```
```
Assistant: Of course! I'd be happy to help you find some book suggestions. Can you please let me know what genre or type of book you're interested in?
```
```
User: I'm looking for suggestions about sci-fi
```
```
Great! Here are a few sci-fi book suggestions across different subgenres:

1. "Dune" by Frank Herbert: This classic novel is ...
```



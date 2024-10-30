from fastapi import FastAPI
import nsfw

app = FastAPI()


@app.get("/")
async def root(file_path: str, file_name: str):

    result = await nsfw.classify(file_path,file_name)
    return next(iter(result.values()))


@app.get("/hello/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}


@app.on_event("startup")
async def startup_event():
    # Start Kafka consumer in the background
    print("Starting up...")

   # await nsfw.classify()

    # await asyncio.create_task(KafkaConsumer.consume_messages())
    return

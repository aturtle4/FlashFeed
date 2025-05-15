from fastapi import FastAPI
from .news import router as news_router

Base.metadata.create_all(bind=engine)

app = FastAPI()
app.include_router(news_router)
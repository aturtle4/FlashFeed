from pydantic import BaseModel, EmailStr

class UserCreate(BaseModel):
    username: str
    email: EmailStr
    password: str

class UserLogin(BaseModel):
    username: str
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str

class NewsSave(BaseModel):
    title: str
    content: str
    url: str
    category: str
    language: str

class NewsResponse(NewsSave):
    id: int

    class Config:
        orm_mode = True

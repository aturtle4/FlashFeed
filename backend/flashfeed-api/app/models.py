from sqlalchemy import Column, Integer, String, ForeignKey
from sqlalchemy.orm import relationship
from .database import Base

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(255), unique=True, index=True)
    email = Column(String(255), unique=True, index=True)
    hashed_password = Column(String(255))
    is_active = Column(Integer, default=1)

class SavedNews(Base):
    __tablename__ = "saved_news"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    title = Column(String(500))       # Add length here
    content = Column(String(2000))    # Add length here
    url = Column(String(1000))        # And here
    category = Column(String(50))     
    language = Column(String(20))

    user = relationship("User", backref="saved_articles")

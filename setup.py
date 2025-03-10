from setuptools import setup, find_packages

setup(
    name="py4jasper",
    version="1.0.0",
    description="Python package for generating reports using Jasper",
    author="Sanskar Gandhi",
    author_email="sanskar.gandho@outlook.com",
    packages=find_packages(),
    install_requires=["py4j"],  
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
    ],
    python_requires=">=3.7",
)

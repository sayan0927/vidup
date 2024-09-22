from nudenet import NudeClassifier
from nudenet import NudeDetector
import asyncio


# Initialize NudeNet classifier


# Open the video file


async def classify(file_path,file_name):
    classifier = NudeClassifier()
    file_location = file_path+"\\"+file_name

    #await asyncio.sleep(20)

    result = await asyncio.to_thread(classifier.classify, file_location)
    return result




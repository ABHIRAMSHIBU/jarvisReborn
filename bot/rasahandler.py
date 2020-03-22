class chatbot:
    def __init__(self,loop):
        from rasa_nlu.model import Interpreter
        from rasa.core.agent import Agent
        from rasa.core.interpreter import RasaNLUInterpreter
        import os
        import asyncio as asyncio
        self.asyncio=asyncio
        self.loop=loop
        # os.chdir("chatbot")
        model_path = "chatbot/models/current"
        self.agent = Agent.load(model_path)
        self.interpreter = Interpreter.load(model_path+"/nlu")
    def predict_nlu(self,string):
        return self.interpreter.parse(string)
    def predict_chat(self,string):
        loop = self.loop
        return loop.run_until_complete(self.agent.handle_text(string))
# Chat bot test code
# obj=chatbot()
# try:
#     while(True):
#         userinput=input("You>")
#         print("bot>",obj.predict_nlu(userinput))
#         print("bot>",obj.predict_chat(userinput))
# except:
#     print("Bye...")

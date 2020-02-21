class chatbot:
    def __init__(self):
        from rasa_nlu.model import Interpreter
        from rasa.core.agent import Agent
        from rasa.core.interpreter import RasaNLUInterpreter
        import os
        import asyncio as asyncio
        self.asyncio=asyncio
        # os.chdir("chatbot")
        model_path = "chatbot/models/current"
        self.agent = Agent.load(model_path)
        self.interpreter = Interpreter.load(model_path+"/nlu")
    def predict_nlu(self,string):
        return self.interpreter.parse(string)
    def predict_chat(self,string):
        return self.asyncio.run(self.agent.handle_text(string))
# Chat bot test code
# obj=chatbot()
# try:
#     while(True):
#         userinput=input("You>")
#         print("bot>",obj.predict_nlu(userinput))
#         print("bot>",obj.predict_chat(userinput))
# except:
#     print("Bye...")
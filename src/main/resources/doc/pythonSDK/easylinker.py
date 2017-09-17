# -*- coding: utf-8 -*-

import threading

import paho.mqtt.client as mqtt

SERVER_HOST = "localhost"
SERVER_PORT = 1883


class EasyLinkerApp(threading.Thread):
    def on_connect(self, client, userdata, flags, rc):
        print('Device  [%s]  Connected Success!' % self.device_id)
        client.subscribe("device/" + self.device_id)
        

    def disconnect(self):

        self.mqtt_client.disconnect()
        print('Device  [%s]  Disconnected!' % self.device_id)

    def run(self):
        try:
            # 请根据实际情况改变MQTT代理服务器的IP地址
            self.mqtt_client.connect(SERVER_HOST, SERVER_PORT, 60)
            self.mqtt_client.loop_forever()
        except Exception:
            self.disconnect()

    def __init__(self, device_id, on_message):
        self.device_id = device_id
        self.server_host = SERVER_HOST
        self.server_port = SERVER_PORT
        self.mqtt_client = mqtt.Client()
        self.mqtt_client.on_connect = self.on_connect
        self.mqtt_client.on_message = on_message
        self.mqtt_client.on_disconnect=self.disconnect
        self.mqtt_client.username_pw_set(device_id)
        self.dev_mode = False

    def publish(self, message):
        self.mqtt_client.publish(self.device_id, message)


# 消息推送回调函数
def on_message(client, userdata, msg):
    print("receiveed message:" + str(msg.payload))


app = EasyLinkerApp("c56ff6d3-541d-4862-b3db-a26f44c921d2", on_message)
app.run()
app.publish("hello")

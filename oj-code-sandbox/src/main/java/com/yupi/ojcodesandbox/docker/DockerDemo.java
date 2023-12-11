package com.yupi.ojcodesandbox.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

import java.util.List;

public class DockerDemo {
    public static void main(String[] args) throws InterruptedException {
        //获取默认的Docker Client
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        //拉取镜像
        String images = "nginx:latest";
        /* PullImageCmd pullImageCmd = dockerClient.pullImageCmd(images);
        PullImageResultCallback pullImageResultCallback =new PullImageResultCallback(){
            @Override
            public void onNext(PullResponseItem item) {
                System.out.println("下载镜像"+item.getStatus());
                super.onNext(item);
            }
        };
        pullImageCmd
                .exec(pullImageResultCallback)
                .awaitCompletion();
        System.out.println("下载完成");*/

        //创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(images);
        CreateContainerResponse createContainerResponse = containerCmd
                .withCmd("echo", "hello Docker")
                .exec();
        System.out.println(createContainerResponse);
        String containerId = createContainerResponse.getId();

        //查看容器状态
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        List<Container> containerList = listContainersCmd.withShowAll(true).exec();
        for (Container container : containerList) {
            System.out.println(container);
        }

        //启动容器
        dockerClient.startContainerCmd(containerId).exec();

        //查看日志
        LogContainerResultCallback logContainerResultCallback = new LogContainerResultCallback(){
            @Override
            public void onNext(Frame item) {
                System.out.println(item.getStreamType());
                System.out.println("日志："+new String(item.getPayload()));
                super.onNext(item);
            }
        };
        dockerClient.logContainerCmd(containerId)
                .withStdErr(true)
                .withStdOut(true)
                .exec(logContainerResultCallback)
                .awaitCompletion();

        //删除容器
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();

        //删除镜像
        dockerClient.removeImageCmd(images).exec();
    }
}

package time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Created by Franklin on 7/1/2017.
 */
public class TimeClient {
    // We need a Time Client because we need to be able to interpret the Time integer provided.

    public static void main(String[] args) throws Exception {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // The server process and pipeline is built around the bootstrap.
            Bootstrap b = new Bootstrap();  // Like ServerBootstrap, but for non-server channels
            b.group(workerGroup);  // We assign working groups.
            // We specify the type of socket/channel
            b.channel(NioSocketChannel.class);  // Not NioServerSocketChannel, because it's a client.
            b.option(ChannelOption.SO_KEEPALIVE, true);  // We specify how the channel operates.
            b.handler(new ChannelInitializer<SocketChannel>() {  // And how it's handled.
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(
                            new TimeDecoder(),
                            new TimeClientHandler()
                    );
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();  // Connect instead of bind method.
            f.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    System.out.println("Connected!");
                }
            });
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

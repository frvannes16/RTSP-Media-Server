package time;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Franklin on 7/1/2017.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("Channel Active");
        // ChannelActive is invoked when a connection is established and ready to generate traffic.

        final ChannelFuture f = ctx.writeAndFlush(new UnixTime());  // ChannelFuture represents an I/O op that hasn't yet occurred
        f.addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

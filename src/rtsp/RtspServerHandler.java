package rtsp;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.rtsp.RtspHeaderNames;
import io.netty.handler.codec.rtsp.RtspHeaderValues;
import io.netty.handler.codec.rtsp.RtspResponseStatuses;
import io.netty.handler.codec.rtsp.RtspVersions;

/**
 * Created by Franklin on 7/1/2017.
 */
public class RtspServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DefaultHttpRequest message;
        if (msg instanceof DefaultHttpRequest) {
            message = (DefaultHttpRequest) msg;  // Has been decoded in pipeline.
            System.out.println("C -> S");
            System.out.println(message);
            System.out.println();
        } else {
            return;
        }
        // Now we can parse the request and craft our response.
        FullHttpResponse rep = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0,  RtspResponseStatuses.NOT_FOUND);
        if (message.method() == HttpMethod.OPTIONS){
            rep.setStatus(HttpResponseStatus.OK);
            rep.headers().add(RtspHeaderNames.PUBLIC, "DESCRIBE, SETUP, TEARDOWN, PLAY, PAUSE");
        }

        if (message.headers().contains(HttpHeaderValues.KEEP_ALIVE)){
            rep.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.write(rep);
        } else {
            ctx.write(rep).addListener(ChannelFutureListener.CLOSE);
        }



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

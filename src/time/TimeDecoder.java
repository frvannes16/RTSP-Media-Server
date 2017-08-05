package time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Franklin on 7/1/2017.
 */
public class TimeDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        // So the context provided ensures that when run asynchronously, this method is always checking the length
        // of the ByteBuf for the same connection. So once it has reached a length of 4 do we return the frame of 4 bytes.
        if (byteBuf.readableBytes() < 4) {
            return;
        }

        out.add(new UnixTime(byteBuf.readUnsignedInt()));
    }
}

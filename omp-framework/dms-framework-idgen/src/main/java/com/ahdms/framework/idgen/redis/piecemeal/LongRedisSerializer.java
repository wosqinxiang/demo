package com.ahdms.framework.idgen.redis.piecemeal;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

/**
 * redis serializer for Long type
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/13 15:56
 */
public class LongRedisSerializer implements RedisSerializer<Long> {

    private final Charset charset;

    public LongRedisSerializer() {
        this(Charset.forName("UTF8"));
    }

    public LongRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public byte[] serialize(Long t) throws SerializationException {
        return String.valueOf(t).getBytes(charset);
    }

    @Override
    public Long deserialize(byte[] bytes) throws SerializationException {
        return Long.valueOf(new String(bytes, charset));
    }

}

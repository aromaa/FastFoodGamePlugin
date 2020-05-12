package fi.joniaromaa.FastFoodGamePlugin.Utils;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

public class StringUtils
{
	public static void writeCharSequence(ByteBuf buffer, CharSequence cs)
	{
		byte[] bytes = cs.toString().getBytes();
		
		StringUtils.write7BitEncodedInt(buffer, bytes.length);
		buffer.writeBytes(bytes);
	}
	
	public static void write7BitEncodedInt(ByteBuf buffer, int value)
	{
		int v = value;
		while (v >= 0x80)
		{
			buffer.writeByte(v | 0x80);
			v >>= 7;
		}
		
		buffer.writeByte(v);
	}
	
	public static String readString(ByteBuf buffer)
	{
		int count = StringUtils.read7BitEncodedInt(buffer);
		
		return buffer.readBytes(count).toString(Charset.defaultCharset());
	}
	
	public static int read7BitEncodedInt(ByteBuf buffer)
	{
		int count = 0;
		int shift = 0;
		byte b;
		
		do
		{
			if (shift == 5 * 7)
				return 0;
			
			b = buffer.readByte();
			count |= (b & 0x7F) << shift;
			shift += 7;
		}
		while ((b & 0x80) != 0);
		
		return count;
	}
}

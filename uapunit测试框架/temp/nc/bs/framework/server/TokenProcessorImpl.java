package nc.bs.framework.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nc.bs.framework.component.ContextAwareComponent;
import nc.bs.framework.core.ComponentContext;
import nc.bs.framework.core.Container;
import nc.bs.framework.exception.FrameworkRuntimeException;
import nc.bs.framework.exception.FrameworkSecurityException;
import nc.bs.framework.server.util.KeyUtil;

import org.granite.lang.util.HexEncoder;

/**
 * 
 * @author hgy
 * 
 */
public class TokenProcessorImpl implements ContextAwareComponent<Container>,
		ITokenProcessor {

	byte[] tokenSeed;

	private ComponentContext<Container> ctx;

	private byte[] getTokenSeed() {
		if (tokenSeed == null) {
			String s = "dfdfasdfafeffg";

//			if (s == null) {
//				throw new FrameworkRuntimeException("no token seed");
//			}

			HexEncoder e = new HexEncoder();

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			try {
				e.decode(s, out);
			} catch (IOException e1) {
				throw new FrameworkRuntimeException("error tokend seed", e1);
			}

			tokenSeed = out.toByteArray();

		}

		return tokenSeed;
	}

	@Override
	public void setComponentContext(ComponentContext<Container> context) {
		this.ctx = context;
	}

	public TokenProcessorImpl() {

	}

	@Override
	public byte[] genToken(byte[] sys, byte[] origin) {

		if (sys == null) {
			throw new FrameworkSecurityException("invalid system");
		}

		if (origin == null) {
			throw new FrameworkSecurityException("invalid token");
		}

		long now = System.currentTimeMillis();

	
		byte[] tokenBytes = new byte[8 + origin.length];

		writeLong(tokenBytes, now);

		System.arraycopy(origin, 0, tokenBytes, 8, origin.length);

	

		byte[] md5 = KeyUtil.md5(getTokenSeed(), tokenBytes);

		byte[] nbytes = new byte[origin.length + md5.length + 8];

		System.arraycopy(tokenBytes, 0, nbytes, 0, origin.length + 8);

		System.arraycopy(md5, 0, nbytes, 8 + origin.length, md5.length);

		return nbytes;
	}

	@Override
	public byte[] verifyToken(byte[] token) {
		if (token == null || token.length < 20) {
			return null;
		}

		byte[] md5 = new byte[20];

		System.arraycopy(token, token.length - 20, md5, 0, 20);

		
		byte[] tbytes = new byte[token.length - 20];

		System.arraycopy(token, 0, tbytes, 0, tbytes.length);

		if (KeyUtil.verifyMD5(getTokenSeed(), tbytes, md5)) {
			return token;
		}

		return null;

	}

	private final void writeLong(byte[] writeBuffer, long v) {
		writeBuffer[0] = (byte) (v >>> 56);
		writeBuffer[1] = (byte) (v >>> 48);
		writeBuffer[2] = (byte) (v >>> 40);
		writeBuffer[3] = (byte) (v >>> 32);
		writeBuffer[4] = (byte) (v >>> 24);
		writeBuffer[5] = (byte) (v >>> 16);
		writeBuffer[6] = (byte) (v >>> 8);
		writeBuffer[7] = (byte) (v >>> 0);
	}

}
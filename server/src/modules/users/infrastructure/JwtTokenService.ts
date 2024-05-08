import { TokenDecoded, TokenEncoded, Session, TokenService, PartialSession, TokenValidateResult } from "../core/domain/TokenService";

import { decode, encode, TAlgorithm } from "jwt-simple";

export class JwtTokenService implements TokenService {
  constructor(private secretKey: string) { }

  public validate(token: string): TokenValidateResult {
    const tokenDecoded = this.decode(token);

    if (tokenDecoded.type === "integrity-error" || tokenDecoded.type === "invalid-token") {
      return {
        valid: false,
        message: `Error al validar el token: ${tokenDecoded.type}`,
      };
    }

    return {
      valid: true,
      tokenDecoded,
    }
  }

  public encode(partialSession: PartialSession): TokenEncoded {
    const algorithm: TAlgorithm = "HS512";
    const issued = Date.now();
    const fifteenMinutesInMs = 15 * 60 * 1000;
    const expires = issued + fifteenMinutesInMs;
    const session: Session = {
      ...partialSession,
      issued: issued,
      expires: expires
    };

    return {
      token: encode(session, this.secretKey, algorithm),
      issued: issued,
      expires: expires
    };
  }

  public decode(token: string): TokenDecoded {
    // Always use HS512 to decode the token
    const algorithm: TAlgorithm = "HS512";

    let result: Session;

    try {
      result = decode(token, this.secretKey, false, algorithm);
    } catch (_e) {
      const e: Error = _e as Error;

      // These error strings can be found here:
      // https://github.com/hokaccha/node-jwt-simple/blob/c58bfe5e5bb049015fcd55be5fc1b2d5c652dbcd/lib/jwt.js
      if (e.message === "No token supplied" || e.message === "Not enough or too many segments") {
        return {
          type: "invalid-token"
        };
      }

      if (e.message === "Signature verification failed" || e.message === "Algorithm not supported") {
        return {
          type: "integrity-error"
        };
      }

      // Handle json parse errors, thrown when the payload is nonsense
      if (e.message.indexOf("Unexpected token") === 0) {
        return {
          type: "invalid-token"
        };
      }

      throw e;
    }

    return {
      type: "valid-token",
      session: result
    }
  }
}
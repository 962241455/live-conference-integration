package com.cmnt.dbpick.gateway.server.security;

/*import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;*/

import lombok.extern.slf4j.Slf4j;

/**
 * 集成oauth相关代码，从server项目中拷贝
 *
 * @Description: 重写原oauth2的UserInfoTokenServices类,
 *               为了处理区分oauth服务器异常还是accesstoken错误
 * @author: zk
 * @date: 2020年10月15日
 */
@Slf4j
public class UserInfoTokenService{ /*implements ResourceServerTokenServices {

	private final String userInfoEndpointUrl;
	private final String clientId;
	private OAuth2RestOperations restTemplate;
	private String tokenType = "Bearer";
	private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();

	public UserInfoTokenService(String userInfoEndpointUrl, String clientId) {
		this.userInfoEndpointUrl = userInfoEndpointUrl;
		this.clientId = clientId;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public void setRestTemplate(OAuth2RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
		Assert.notNull(authoritiesExtractor, "AuthoritiesExtractor must not be null");
		this.authoritiesExtractor = authoritiesExtractor;
	}

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		// 如果token不为空才会进入该方法
		Map<String, Object> map = this.getMap(this.userInfoEndpointUrl, accessToken);
		if (map.containsKey("error")) {
			// 如果token不正确，进入这里
			String error = map.get("error").toString();
			log.debug("userinfo returned error: {},accessToken:{}", error, accessToken);
			throw new InvalidTokenException(error);
		} else {
			// 权限验证，查询账号是否被封号，商城后台这里暂时不做处理
			*//*
			 * try { OauthService oauthService = (OauthService)
			 * SpringBeanFactoryUtils.getBean("oauthServiceImpl"); if(map.get("data") !=
			 * null){ Map data = (Map)map.get("data"); String account = data.get("account")
			 * == null ? "" : data.get("account").toString();
			 * if(StringUtils.isNotBlank(account)){ String msg =
			 * oauthService.checkAccountIsCanLogin(account,"en", 1 << 6);
			 * if(StringUtils.isNotBlank(msg)){//账号被封号
			 * logger.info("account is forbiden login,account:{},msg:{},accessToken:{}"
			 * ,account,msg,accessToken); throw new
			 * InvalidTokenException("forbiden account"); } } } }catch
			 * (InvalidTokenException ie){ throw ie; }catch (Exception e){
			 * logger.error("deal forbiden account error,MAP:{}",map,e); }
			 *//*
			return this.extractAuthentication(map);
		}
	}

	private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
		Object principal = this.getPrincipal(map);
		List<GrantedAuthority> authorities = this.authoritiesExtractor.extractAuthorities(map);
		OAuth2Request request = new OAuth2Request((Map) null, this.clientId, (Collection) null, true, (Set) null,
				(Set) null, (String) null, (Set) null, (Map) null);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "N/A",
				authorities);
		token.setDetails(map);
		return new OAuth2Authentication(request, token);
	}

	protected Object getPrincipal(Map<String, Object> map) {
		CustomPrincipal customPrincipal = new CustomPrincipal();
		BeanUtil.copyProperties(map.get("data"), customPrincipal);
		return customPrincipal == null ? "unknown" : customPrincipal;
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token:" + accessToken);
	}

	private Map<String, Object> getMap(String path, String accessToken) {
		try {
			OAuth2RestOperations restTemplate = this.restTemplate;
			if (restTemplate == null) {
				BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
				resource.setClientId(this.clientId);
				restTemplate = new OAuth2RestTemplate(resource);
			}

			OAuth2AccessToken existingToken = ((OAuth2RestOperations) restTemplate).getOAuth2ClientContext()
					.getAccessToken();
			if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
				DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
				token.setTokenType(this.tokenType);
				((OAuth2RestOperations) restTemplate).getOAuth2ClientContext().setAccessToken(token);
			}
			return (Map) ((OAuth2RestOperations) restTemplate).getForEntity(path, Map.class, new Object[0]).getBody();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			String msg = ResultCode.SERVICE_FAILED;
			if (e instanceof HttpClientErrorException) {
				if (((HttpClientErrorException) e).getStatusCode().value() == 401) {// accesstoken错误
					msg = ResultCode.TOKEN_EXPIRED;
				}
			}
			return Collections.singletonMap("error", msg);
		}
	}*/
}

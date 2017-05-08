/*
 * Copyright (C) 2016 Francisco José Montiel Navarro.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ernest.com.meilidemo.util.net.cookie;


import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class PersistentCookieJar implements CookieJar {

	private static PersistentCookieJar instance;

	private CookiePersistor persistor;

	private PersistentCookieJar(CookiePersistor persistor) {
		this.persistor = persistor;

	}

	/**
	 *
	 * @param context applicationCotext
	 * @param sharePreferenceCookieString sharePreference key
     * @return
     */
	public static PersistentCookieJar getInstance(Context context, String sharePreferenceCookieString) {
		if (instance == null) {
			synchronized (PersistentCookieJar.class) {
				if (instance == null) {
					instance = new PersistentCookieJar(new SharedPrefsCookiePersistor(context, sharePreferenceCookieString));
				}
			}
		}
		return instance;
	}

	@Override
	synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
		if (url.toString().endsWith("login.json")) {
			persistor.saveAll(cookies);
		}
	}

	@Override
	synchronized public List<Cookie> loadForRequest(HttpUrl url) {
		List<Cookie> cookies = persistor.loadAll();
		return cookies;
	}

	private static boolean isCookieExpired(Cookie cookie) {
		return cookie.expiresAt() < System.currentTimeMillis();
	}

	synchronized public void clear() {
		persistor.clear();
	}
}

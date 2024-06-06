import { goto } from '$app/navigation';

import createClient from 'openapi-fetch';

import type { paths, components } from '$lib/backend/apiV1/schema';

import toastr from 'toastr';
import 'toastr/build/toastr.css';

toastr.options = {
	showDuration: 300,
	hideDuration: 300,
	timeOut: 3000,
	extendedTimeOut: 1000,
	positionClass: 'toast-top-right'
};

type Client = ReturnType<typeof createClient<paths>>;

const client: Client = createClient<paths>({
	baseUrl: import.meta.env.VITE_CORE_API_BASE_URL,
	credentials: 'include'
});

class Rq {
	public member: components['schemas']['MemberDto'];

	constructor() {
		this.member = this.makeReactivityMember();
	}

	// 회원
	private makeReactivityMember() {
		let id = $state(0);
		let createDate = $state('');
		let modifyDate = $state('');
		let username = $state('');
		let nickname = $state('');
		let profileImgUrl = $state('');
		let authorities: string[] = $state([]);
		let social = $state(false);

		return {
			get id() {
				return id;
			},
			set id(value: number) {
				id = value;
			},
			get createDate() {
				return createDate;
			},
			set createDate(value: string) {
				createDate = value;
			},
			get modifyDate() {
				return modifyDate;
			},
			set modifyDate(value: string) {
				modifyDate = value;
			},
			get username() {
				return username;
			},
			set username(value: string) {
				username = value;
			},
			get nickname() {
				return nickname;
			},
			set nickname(value: string) {
				nickname = value;
			},
			get profileImgUrl() {
				return profileImgUrl;
			},
			set profileImgUrl(value: string) {
				profileImgUrl = value;
			},
			get authorities() {
				return authorities;
			},
			set authorities(value: string[]) {
				authorities = value;
			},
			get social() {
				return social;
			},
			set social(value: boolean) {
				social = value;
			}
		};
	}

	public setLogined(member: components['schemas']['MemberDto']) {
		Object.assign(this.member, member);
	}

	public setLogout() {
		this.member.id = 0;
		this.member.createDate = '';
		this.member.modifyDate = '';
		this.member.username = '';
		this.member.nickname = '';
		this.member.profileImgUrl = '';
		this.member.authorities = [];
		this.member.social = false;
	}

	public isLogin() {
		return this.member.id !== 0;
	}

	public isLogout() {
		return !this.isLogin();
	}

	public async initAuth() {
		const { data } = await this.getClient().GET('/api/v1/members/me');

		if (data) {
			this.setLogined(data.data.item);
		}
	}

	public async logoutAndRedirect(url: string) {
		await this.getClient().DELETE('/api/v1/members/logout');

		this.setLogout();
		this.replace(url);
	}

	public getKakaoLoginUrl() {
		return `${
			import.meta.env.VITE_CORE_API_BASE_URL
		}/member/socialLogin/kakao?redirectUrl=${encodeURIComponent(
			import.meta.env.VITE_CORE_FRONT_BASE_URL
		)}/member/socialLoginCallback?provierTypeCode=kakao`;
	}

	// URL
	public goto(url: string) {
		goto(url);
	}

	public replace(url: string) {
		goto(url, { replaceState: true });
	}

	public reload() {
		this.replace('/redirect?url=' + window.location.href);
	}

	// MSG, REDIRECT
	public msgAndRedirect(
		data: { msg: string } | undefined,
		error: { msg: string } | undefined,
		url: string,
		callback?: () => void
	) {
		if (data) this.msgInfo(data.msg);
		if (error) this.msgError(error.msg);

		this.replace(url);

		if (callback) window.setTimeout(callback, 100);
	}

	public msgInfo(message: string) {
		toastr.info(message, '', toastr.options);
	}

	public msgError(message: string) {
		toastr.error(message, '', toastr.options);
	}

	// OPENAPI FETCH
	public getClient() {
		return client;
	}

	public getClientWithFetch(fetch: any) {
		return createClient<paths>({
			baseUrl: import.meta.env.VITE_CORE_API_BASE_URL,
			credentials: 'include',
			fetch
		});
	}
}

const rq = new Rq();

export default rq;

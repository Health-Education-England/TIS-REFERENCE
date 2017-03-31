export class Site {
	constructor(public id?: number,
				public siteCode?: string,
				public siteName?: string,
				public address?: string,
				public postCode?: string,
				public siteKnownAs?: string,
				public siteNumber?: string,
				public organisationalUnit?: string,
				public localOfficeId?: number,
				public trustCodeId?: number,) {
	}
}

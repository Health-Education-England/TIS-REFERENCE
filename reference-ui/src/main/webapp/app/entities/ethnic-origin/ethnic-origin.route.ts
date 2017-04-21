import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {EthnicOriginComponent} from "./ethnic-origin.component";
import {EthnicOriginDetailComponent} from "./ethnic-origin-detail.component";
import {EthnicOriginPopupComponent} from "./ethnic-origin-dialog.component";
import {EthnicOriginDeletePopupComponent} from "./ethnic-origin-delete-dialog.component";


export const ethnicOriginRoute: Routes = [
	{
		path: 'ethnic-origin',
		component: EthnicOriginComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.ethnicOrigin.home.title'
		}
	}, {
		path: 'ethnic-origin/:id',
		component: EthnicOriginDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.ethnicOrigin.home.title'
		}
	}
];

export const ethnicOriginPopupRoute: Routes = [
	{
		path: 'ethnic-origin-new',
		component: EthnicOriginPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.ethnicOrigin.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'ethnic-origin/:id/edit',
		component: EthnicOriginPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.ethnicOrigin.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'ethnic-origin/:id/delete',
		component: EthnicOriginDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.ethnicOrigin.home.title'
		},
		outlet: 'popup'
	}
];

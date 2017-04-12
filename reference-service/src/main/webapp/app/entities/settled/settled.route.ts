import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {SettledComponent} from "./settled.component";
import {SettledDetailComponent} from "./settled-detail.component";
import {SettledPopupComponent} from "./settled-dialog.component";
import {SettledDeletePopupComponent} from "./settled-delete-dialog.component";


export const settledRoute: Routes = [
	{
		path: 'settled',
		component: SettledComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.settled.home.title'
		}
	}, {
		path: 'settled/:id',
		component: SettledDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.settled.home.title'
		}
	}
];

export const settledPopupRoute: Routes = [
	{
		path: 'settled-new',
		component: SettledPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.settled.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'settled/:id/edit',
		component: SettledPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.settled.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'settled/:id/delete',
		component: SettledDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.settled.home.title'
		},
		outlet: 'popup'
	}
];

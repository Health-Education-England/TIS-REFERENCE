import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {DBCComponent} from "./dbc.component";
import {DBCDetailComponent} from "./dbc-detail.component";
import {DBCPopupComponent} from "./dbc-dialog.component";
import {DBCDeletePopupComponent} from "./dbc-delete-dialog.component";


export const dBCRoute: Routes = [
	{
		path: 'dbc',
		component: DBCComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.dbc.home.title'
		}
	}, {
		path: 'dbc/:id',
		component: DBCDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.dbc.home.title'
		}
	}
];

export const dBCPopupRoute: Routes = [
	{
		path: 'dbc-new',
		component: DBCPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.dbc.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'dbc/:id/edit',
		component: DBCPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.dbc.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'dbc/:id/delete',
		component: DBCDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.dbc.home.title'
		},
		outlet: 'popup'
	}
];

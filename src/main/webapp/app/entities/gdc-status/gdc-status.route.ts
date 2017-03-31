import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {GdcStatusComponent} from "./gdc-status.component";
import {GdcStatusDetailComponent} from "./gdc-status-detail.component";
import {GdcStatusPopupComponent} from "./gdc-status-dialog.component";
import {GdcStatusDeletePopupComponent} from "./gdc-status-delete-dialog.component";


export const gdcStatusRoute: Routes = [
	{
		path: 'gdc-status',
		component: GdcStatusComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gdcStatus.home.title'
		}
	}, {
		path: 'gdc-status/:id',
		component: GdcStatusDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gdcStatus.home.title'
		}
	}
];

export const gdcStatusPopupRoute: Routes = [
	{
		path: 'gdc-status-new',
		component: GdcStatusPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gdcStatus.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'gdc-status/:id/edit',
		component: GdcStatusPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gdcStatus.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'gdc-status/:id/delete',
		component: GdcStatusDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.gdcStatus.home.title'
		},
		outlet: 'popup'
	}
];

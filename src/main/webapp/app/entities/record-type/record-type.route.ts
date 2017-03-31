import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {RecordTypeComponent} from "./record-type.component";
import {RecordTypeDetailComponent} from "./record-type-detail.component";
import {RecordTypePopupComponent} from "./record-type-dialog.component";
import {RecordTypeDeletePopupComponent} from "./record-type-delete-dialog.component";


export const recordTypeRoute: Routes = [
	{
		path: 'record-type',
		component: RecordTypeComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.recordType.home.title'
		}
	}, {
		path: 'record-type/:id',
		component: RecordTypeDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.recordType.home.title'
		}
	}
];

export const recordTypePopupRoute: Routes = [
	{
		path: 'record-type-new',
		component: RecordTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.recordType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'record-type/:id/edit',
		component: RecordTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.recordType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'record-type/:id/delete',
		component: RecordTypeDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.recordType.home.title'
		},
		outlet: 'popup'
	}
];

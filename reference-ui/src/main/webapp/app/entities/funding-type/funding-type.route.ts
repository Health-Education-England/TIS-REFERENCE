import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {FundingTypeComponent} from "./funding-type.component";
import {FundingTypeDetailComponent} from "./funding-type-detail.component";
import {FundingTypePopupComponent} from "./funding-type-dialog.component";
import {FundingTypeDeletePopupComponent} from "./funding-type-delete-dialog.component";


export const fundingTypeRoute: Routes = [
	{
		path: 'funding-type',
		component: FundingTypeComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.fundingType.home.title'
		}
	}, {
		path: 'funding-type/:id',
		component: FundingTypeDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.fundingType.home.title'
		}
	}
];

export const fundingTypePopupRoute: Routes = [
	{
		path: 'funding-type-new',
		component: FundingTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.fundingType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'funding-type/:id/edit',
		component: FundingTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.fundingType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'funding-type/:id/delete',
		component: FundingTypeDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.fundingType.home.title'
		},
		outlet: 'popup'
	}
];

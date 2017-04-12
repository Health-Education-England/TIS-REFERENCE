import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PlacementTypeComponent} from "./placement-type.component";
import {PlacementTypeDetailComponent} from "./placement-type-detail.component";
import {PlacementTypePopupComponent} from "./placement-type-dialog.component";
import {PlacementTypeDeletePopupComponent} from "./placement-type-delete-dialog.component";


export const placementTypeRoute: Routes = [
	{
		path: 'placement-type',
		component: PlacementTypeComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.placementType.home.title'
		}
	}, {
		path: 'placement-type/:id',
		component: PlacementTypeDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.placementType.home.title'
		}
	}
];

export const placementTypePopupRoute: Routes = [
	{
		path: 'placement-type-new',
		component: PlacementTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.placementType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'placement-type/:id/edit',
		component: PlacementTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.placementType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'placement-type/:id/delete',
		component: PlacementTypeDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.placementType.home.title'
		},
		outlet: 'popup'
	}
];

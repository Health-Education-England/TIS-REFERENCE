import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	PlacementTypeService,
	PlacementTypePopupService,
	PlacementTypeComponent,
	PlacementTypeDetailComponent,
	PlacementTypeDialogComponent,
	PlacementTypePopupComponent,
	PlacementTypeDeletePopupComponent,
	PlacementTypeDeleteDialogComponent,
	placementTypeRoute,
	placementTypePopupRoute
} from "./";

let ENTITY_STATES = [
	...placementTypeRoute,
	...placementTypePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		PlacementTypeComponent,
		PlacementTypeDetailComponent,
		PlacementTypeDialogComponent,
		PlacementTypeDeleteDialogComponent,
		PlacementTypePopupComponent,
		PlacementTypeDeletePopupComponent,
	],
	entryComponents: [
		PlacementTypeComponent,
		PlacementTypeDialogComponent,
		PlacementTypePopupComponent,
		PlacementTypeDeleteDialogComponent,
		PlacementTypeDeletePopupComponent,
	],
	providers: [
		PlacementTypeService,
		PlacementTypePopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferencePlacementTypeModule {
}

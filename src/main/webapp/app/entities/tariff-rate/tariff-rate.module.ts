import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	TariffRateService,
	TariffRatePopupService,
	TariffRateComponent,
	TariffRateDetailComponent,
	TariffRateDialogComponent,
	TariffRatePopupComponent,
	TariffRateDeletePopupComponent,
	TariffRateDeleteDialogComponent,
	tariffRateRoute,
	tariffRatePopupRoute,
	TariffRateResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...tariffRateRoute,
	...tariffRatePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		TariffRateComponent,
		TariffRateDetailComponent,
		TariffRateDialogComponent,
		TariffRateDeleteDialogComponent,
		TariffRatePopupComponent,
		TariffRateDeletePopupComponent,
	],
	entryComponents: [
		TariffRateComponent,
		TariffRateDialogComponent,
		TariffRatePopupComponent,
		TariffRateDeleteDialogComponent,
		TariffRateDeletePopupComponent,
	],
	providers: [
		TariffRateService,
		TariffRatePopupService,
		TariffRateResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceTariffRateModule {
}

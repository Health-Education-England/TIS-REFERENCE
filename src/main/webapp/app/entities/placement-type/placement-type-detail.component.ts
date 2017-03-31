import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {PlacementType} from "./placement-type.model";
import {PlacementTypeService} from "./placement-type.service";

@Component({
	selector: 'jhi-placement-type-detail',
	templateUrl: './placement-type-detail.component.html'
})
export class PlacementTypeDetailComponent implements OnInit, OnDestroy {

	placementType: PlacementType;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private placementTypeService: PlacementTypeService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['placementType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.placementTypeService.find(id).subscribe(placementType => {
			this.placementType = placementType;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}

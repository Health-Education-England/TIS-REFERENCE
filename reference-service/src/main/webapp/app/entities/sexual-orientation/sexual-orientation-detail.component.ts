import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {SexualOrientation} from "./sexual-orientation.model";
import {SexualOrientationService} from "./sexual-orientation.service";

@Component({
	selector: 'jhi-sexual-orientation-detail',
	templateUrl: './sexual-orientation-detail.component.html'
})
export class SexualOrientationDetailComponent implements OnInit, OnDestroy {

	sexualOrientation: SexualOrientation;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private sexualOrientationService: SexualOrientationService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['sexualOrientation']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.sexualOrientationService.find(id).subscribe(sexualOrientation => {
			this.sexualOrientation = sexualOrientation;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
